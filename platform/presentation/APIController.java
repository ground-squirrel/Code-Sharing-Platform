package platform.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.business.CodeSharingService;
import platform.business.CodeSnippet;
import platform.business.WrappedSnippet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/api", produces="application/json")
public class APIController {

    @Autowired
    CodeSharingService service;

    @GetMapping("/code/{uuid}")
    @ResponseStatus(code= HttpStatus.OK)
    public WrappedSnippet getCode(@PathVariable String uuid) {
        Optional<CodeSnippet> snippet = service.findByID(uuid);
        if (snippet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            CodeSnippet extracted = snippet.get();

            if (extracted.hasExpired()) {
                service.deleteSnippet(extracted);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            extracted.decreaseViewsAllowed();

            service.saveSnippet(extracted);
            return new WrappedSnippet(extracted);
        }
    }

    @GetMapping("/code/latest")
    @ResponseStatus(code= HttpStatus.OK)
    public List<WrappedSnippet> getLatestCode() {
        List<CodeSnippet> snippets = service.getLatest();

        return snippets.stream().map(WrappedSnippet::new).collect(Collectors.toList());
    }

    @PostMapping("/code/new")
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String,String> addSnippet(@RequestBody WrappedSnippet input) {
        CodeSnippet snippet = input.unwrap();

        String randomUUIDString = UUID.randomUUID().toString();
        snippet.setUuid(randomUUIDString);

        service.saveSnippet(snippet);

        return Map.of("id", randomUUIDString);
    }
}
