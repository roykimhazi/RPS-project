package com.example.rps;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.Instant;

public class StopWatch
{
    Instant startTime, endTime;
    Duration duration;
    boolean isRunning = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void start()
    {
        if (isRunning)
        {
            throw new RuntimeException("Stopwatch is already running.");
        }
        this.isRunning = true;
        startTime = Instant.now();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Duration stop()
    {
        this.endTime = Instant.now();
        if (!isRunning)
        {
            throw new RuntimeException("Stopwatch has not been started yet");
        }
        isRunning = false;
        Duration result = Duration.between(startTime, endTime);
        if (this.duration == null)
        {
            this.duration = result;
        } else {
            this.duration = duration.plus(result);
        }

        return this.getElapsedTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Duration getElapsedTime()
    {
        this.duration = Duration.between(startTime, Instant.now());
        return this.duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reset()
    {
        if (this.isRunning)
        {
            this.stop();
        }
        this.duration = null;
    }
}