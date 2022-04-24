package project.generators;

import project.Rule;

import java.util.List;

public class StochasticBush extends StochasticGenerator {
    public StochasticBush() {
        super(90, 25, 1f, 3f, "Y",
                List.of(
                        new Rule('X', "X[-FFF][+FFF]FX"),
                        new Rule('Y', "YFX[+Y][-Y]")
                ),
                10, 10, 0.01f);
    }
}
