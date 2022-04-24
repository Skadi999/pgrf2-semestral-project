package project.generators;

import project.Rule;

import java.util.List;

public class HillbertCurve extends Generator {
    public HillbertCurve() {
        super(
                270,
                90,
                1f,
                5,
                "X",
                List.of(new Rule('X', "+YF-XFX-FY+"),
                        new Rule('Y', "-XF+YFY+FX-")),
                8
        );
    }
}
