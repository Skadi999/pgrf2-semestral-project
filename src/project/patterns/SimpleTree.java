package project.patterns;

import project.Rule;

import java.util.List;

public class SimpleTree extends Pattern {
    public SimpleTree() {
        super(0f, -0.8f, 90, 45, 1.5f, 2, "F",
                List.of(
                        new Rule('F', "G[+F]-F"),
                        new Rule('G', "GG")
                ));
    }
}

//F[-F]F[+F][F]