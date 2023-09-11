package com.frazik.instructgpt.auto;

import com.frazik.instructgpt.Agent;
import com.frazik.instructgpt.response.Response;
import com.frazik.instructgpt.response.Thought;

import java.util.Scanner;

public class Cli {
    private Agent agent;
    private Scanner scanner;

    public Cli(Agent agent) {
        this.agent = agent;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        Response resp = agent.chat();

        while (true) {
            if (resp.hasThoughts()) {
                Thought thoughts = resp.getThoughts();
                if (thoughts.hasText()) {
                    System.out.println(agent.getName() + ": " + thoughts.getText());
                }
                if (thoughts.hasReasoning()) {
                    System.out.println(agent.getName() + ": Reasoning: " + thoughts.getReasoning());
                }
                if (thoughts.hasPlan()) {
                    for (String plan : thoughts.getPlan().split("\n")) {
                        System.out.println(agent.getName() + ": " + plan);
                    }
                }
                if (thoughts.hasCriticism()) {
                    System.out.println(agent.getName() + ": Criticism: " + thoughts.getCriticism());
                }
                if (thoughts.hasSpeak()) {
                    System.out.println(agent.getName() + ": (voice) " + thoughts.getSpeak());
                }
            }
            if (resp.hasCommand()) {
                System.out.println("Agent wants to execute the following command: \n" + resp.getCommand());
                String yn = "";
                do {
                    System.out.print("(Y/N)? ");
                    yn = scanner.nextLine().toLowerCase().trim();
                } while (!"y".equals(yn) && !"n".equals(yn));
                if ("y".equals(yn)) {
                    resp = agent.chat("GENERATE NEXT COMMAND JSON", true);
                } else {
                    System.out.print("Enter feedback (Why not execute the command?): ");
                    String feedback = scanner.nextLine();
                    resp = agent.chat(feedback, false);
                }
                continue;
            }
            System.out.print("Enter message: ");
            String inp = scanner.nextLine();
            if ("exit".equalsIgnoreCase(inp.trim())) {
                return;
            }
            resp = agent.chat(inp);
        }
    }
}

