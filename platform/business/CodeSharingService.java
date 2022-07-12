package platform.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.persistence.SnippetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CodeSharingService {

    private final SnippetRepository snippetRepository;

    @Autowired
    public CodeSharingService(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public boolean saveSnippet(CodeSnippet snippet) {
        snippetRepository.save(snippet);
        return true;
    }

    public Optional<CodeSnippet> findByID(String uuid) {
        return snippetRepository.findById(uuid);
    }

    public List<CodeSnippet> getLatest() {
        return snippetRepository.findTop10ByAndExpirationDateIsNullAndViewsAllowedIsNullOrderByDateDesc();
    }

    public void deleteSnippet(CodeSnippet snippet) {
        snippetRepository.delete(snippet);
    }
}
