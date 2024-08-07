package com.newsapp24.run;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LoadData implements CommandLineRunner {
    private final LoadDataTask loadDataTask;

    public LoadData(LoadDataTask loadDataTask) {
        this.loadDataTask = loadDataTask;
    }

    @Override
    public void run(String... args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(loadDataTask, 0, 160 * 50 * 1000, TimeUnit.MILLISECONDS);
    }
}
