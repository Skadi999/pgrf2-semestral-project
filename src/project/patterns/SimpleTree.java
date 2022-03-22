package project.patterns;

import project.Rule;

import java.util.List;

public class SimpleTree extends Pattern {
    public SimpleTree() {
        super(0f, -0.8f, 90, 300, 1.2f, 3, "F",
                List.of(new Rule('F', "F[-F]F[+F][F]")));
    }
}

//F[-F]F[+F][F]