package examples;

import com.frazik.instructgpt.Agent;

import java.util.List;

public class ExampleSearch {

    public static void main(String[] args) {

        String name = "Shop assistant";
        String description = "an AI assistant that researches and finds the best car";

        List<String> goals = new java.util.ArrayList<>();
        goals.add("Search for the car on Google");
        goals.add("Write the list of the top 5 best cars and their prices to a file");
        goals.add("Summarize the pros and cons of each car and write it to a different file called 'cars-research.txt'");
        goals.add("There will be no user assistance. Terminate once writing both files is complete.");

        Agent agent = new Agent(name, description, goals, "gpt-3.5-turbo");
        agent.cli();
    }

}
