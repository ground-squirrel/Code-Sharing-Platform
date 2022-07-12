package platform.persistence;

import platform.business.CodeSnippet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SnippetRepository extends CrudRepository<CodeSnippet, String> {

    /**
     * 10 latest snippets, excluding secret ones
     * @return
     */
    List<CodeSnippet> findTop10ByAndExpirationDateIsNullAndViewsAllowedIsNullOrderByDateDesc();

}
