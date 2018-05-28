package com.example.Repositories;

import com.example.Entities.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistics,Long> {

    Statistics findStatisticsById (Long id);
}
