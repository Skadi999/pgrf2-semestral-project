package project.patterns;

import project.Rule;

import java.util.List;

public class KochIsle extends Pattern {
    public KochIsle() {
        super(-0.2f,
                0.3f,
                270,
                90,
                1f,
                5,
                "F+F+F+F",
                List.of(new Rule('F', "F+F-F-FF+F+F-F"))
        );
    }
}
