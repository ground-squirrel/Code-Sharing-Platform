package platform.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import platform.business.CodeSharingService;
import platform.business.CodeSnippet;
import platform.business.WrappedSnippet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(produces = "text/html")
public class WebController {

    @Autowired
    CodeSharingService service;

    @GetMapping("/code/{uuid}")
    @ResponseStatus(code= HttpStatus.OK)
    public String getCode(@PathVariable String uuid, Model model) {
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
            WrappedSnippet snippetWrapped = new WrappedSnippet(extracted);

            model.addAttribute("date", snippetWrapped.getDate());
            model.addAttribute("code", snippetWrapped.getCode());
            model.addAttribute("time", snippetWrapped.getTime());
            model.addAttribute("showTime", null != extracted.getExpirationDate());
            model.addAttribute("views", snippetWrapped.getViews());
            model.addAttribute("showViews", null != extracted.getViewsAllowed());

            return "code";
        }
    }

    @GetMapping("/code/latest")
    @ResponseStatus(code= HttpStatus.OK)
    public String getLatestCode(Model model) {
        List<CodeSnippet> snippets = service.getLatest();
        List<WrappedSnippet> snippetsWrapped = snippets.stream()
                .map(WrappedSnippet::new)
                .collect(Collectors.toList());
        model.addAttribute("snippets", snippetsWrapped);
        return "latest";
    }

    @GetMapping("/code/new")
    @ResponseStatus(code = HttpStatus.OK)
    public String getNewSnippetPage() {
        return "newCode";
    }

    @PostMapping("/code/new")
    @ResponseStatus(code = HttpStatus.OK)
    public void addSnippet(@RequestBody WrappedSnippet input) {
        CodeSnippet snippet = input.unwrap();

        String randomUUIDString = UUID.randomUUID().toString();
        snippet.setUuid(randomUUIDString);

        service.saveSnippet(snippet);
    }
}
