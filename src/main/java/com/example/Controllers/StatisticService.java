package com.example.Controllers;

import com.example.Entities.Statistics;
import com.example.Repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;

    // retorna todos los club de la base de datos club, se llama con la ruta /club
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> getAllStatitics() {

        return statisticRepository.findAll();
        //return statisticRepository.findAll();
    }

    // retorna un determinado club segun la id entregada, se llama con la ruta /club/id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Statistics getStatic(@PathVariable Integer id) {
        Long staticId = id.longValue();
        return statisticRepository.findStatisticsById(staticId);
    }
}
