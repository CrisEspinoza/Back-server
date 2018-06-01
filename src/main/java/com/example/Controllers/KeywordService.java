package com.example.Controllers;


import com.example.Entities.Keyword;
import com.example.Repositories.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/keyword")
public class KeywordService {

    @Autowired
    private  KeywordRepository keywordRepository;

    @PutMapping("/{id}")
    public HttpStatus editarKey(@PathVariable("id") Long id, @RequestBody Keyword keyword){

        Keyword key= keywordRepository.findKeywordById(id);
        if(key != null){
            key.setName_keyword(keyword.getName_keyword());
            keywordRepository.save(key);
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_FOUND;
        }



    }


    @DeleteMapping("/{id}")
    public HttpStatus eliminarKey(@PathVariable("id") Long id){
        Keyword key= keywordRepository.findKeywordById(id);
        if(key != null){
            keywordRepository.delete(key);
            return HttpStatus.OK;
        }
        else {
            return HttpStatus.NOT_FOUND;
        }

    }






}
