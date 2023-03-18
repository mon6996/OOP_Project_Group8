package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration
{
    private static long m;
    private static long n;
    private static long init_plan_min;
    private static long init_plan_sec;
    private static long init_budget;
    private static long init_center_dep;
    private static long plan_rev_min;
    private static long plan_rev_sec;
    private static long rev_cost;
    private static long max_dep;
    private static long interest_pct;

    public static void setConfig(String config)
    {
        Path path = Paths.get("src/configuration_file.txt");
        try
        {
            Files.write(path, config.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        Path configFile = path;
        try (BufferedReader reader = Files.newBufferedReader(configFile))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String key = line.substring(0, line.indexOf('='));
                Long val = Long.valueOf(line.substring(line.indexOf('=') + 1));
                switch (key)
                {
                    case "m" -> m = val;
                    case "n" -> n = val;
                    case "init_plan_min" -> init_plan_min = val;
                    case "init_plan_sec" -> init_plan_sec = val;
                    case "init_budget" -> init_budget = val;
                    case "init_center_dep" -> init_center_dep = val;
                    case "plan_rev_min" -> plan_rev_min = val;
                    case "plan_rev_sec" -> plan_rev_sec = val;
                    case "rev_cost" -> rev_cost = val;
                    case "max_dep" -> max_dep = val;
                    case "interest_pct" -> interest_pct = val;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage() + " : file not found");
        }
    }

    public static void setConfig(Path path)
    {
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String key = line.substring(0, line.indexOf('='));
                Long val = Long.valueOf(line.substring(line.indexOf('=') + 1));
                switch (key)
                {
                    case "m" -> m = val;
                    case "n" -> n = val;
                    case "init_plan_min" -> init_plan_min = val;
                    case "init_plan_sec" -> init_plan_sec = val;
                    case "init_budget" -> init_budget = val;
                    case "init_center_dep" -> init_center_dep = val;
                    case "plan_rev_min" -> plan_rev_min = val;
                    case "plan_rev_sec" -> plan_rev_sec = val;
                    case "rev_cost" -> rev_cost = val;
                    case "max_dep" -> max_dep = val;
                    case "interest_pct" -> interest_pct = val;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage() + " : file not found");
        }
    }

    public static long getM()
    {
        return m;
    }

    public static long getN()
    {
        return n;
    }

    public static long getInit_budget()
    {
        return init_budget;
    }

    public static long getInit_center_dep()
    {
        return init_center_dep;
    }

    public static long getMax_dep()
    {
        return max_dep;
    }

    public static long getInterest_pct()
    {
        return interest_pct;
    }
}
