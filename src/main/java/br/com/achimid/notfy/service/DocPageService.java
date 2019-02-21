package br.com.achimid.notfy.service;

import br.com.achimid.notfy.model.DocPage;
import br.com.achimid.notfy.repository.DocPageRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class DocPageService {

    private DocPageRepository repository;

    public DocPage save(@NonNull DocPage docPage){
        docPage.generateHashHtml();
        docPage.setDateInsert(Calendar.getInstance());
        return repository.save(docPage);
    }

}
