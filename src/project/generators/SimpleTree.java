package project.generators;

import project.Rule;

import java.util.List;

public class SimpleTree extends Generator {
    public SimpleTree() {
        super(90, 45, 1.5f, 2, "F",
                List.of(
                        new Rule('F', "G[+F]-F"),
                        new Rule('G', "GG")
                ),
                10);
    }
}