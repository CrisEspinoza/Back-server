package com.example.Controllers;

import com.example.Entities.Club;
import com.example.Entities.Keyword;
import com.example.Entities.Statistics;
import com.example.Repositories.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/club")
public class ClubService {



    @Autowired
    private ClubRepository clubRepository;

    // retorna todos los club de la base de datos club, se llama con la ruta /club
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Club> getAllClub() {
        List<Club> clubs=  clubRepository.findAll();
        for (Club c: clubs) {
            c.getStatistics().sort(Comparator.comparing(Statistics::getId));
        }
//
        return clubs;
    }

    // retorna un determinado club segun la id entregada, se llama con la ruta /club/id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Club getClub(@PathVariable Integer id) {
        Long staticId = id.longValue();
        Club club= clubRepository.findClubById(staticId);
        club.getStatistics().sort(Comparator.comparing(Statistics::getId));
        return club;
    }

    @PostMapping("/{id}/keyword")
    private HttpStatus addKeyword(@PathVariable Long id,@RequestBody Keyword keyword){
        Club club = clubRepository.findClubById(id);
        if(club != null){
            club.getKeywords().add(keyword);
            clubRepository.save(club);
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.NOT_FOUND;
        }

    }
}
