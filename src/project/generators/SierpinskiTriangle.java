package project.generators;

import project.Rule;

import java.util.List;

public class SierpinskiTriangle extends Generator {
    public SierpinskiTriangle() {
        super(180, 60, 1f, 3f, "F",
                List.of(
                        new Rule('G', "F-G-F"),
                        new Rule('F', "G+F+G")
                ),
                10);
    }
}
