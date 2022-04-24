package project.generators;

import project.Rule;

import java.util.List;

public class StochasticBetterTree extends StochasticGenerator{
    public StochasticBetterTree() {
        super( 75, 20, 1f, 2.5f, "F",
                List.of(
                        new Rule('F', "G+[[F]-F]-G[-GF]+F"),
                        new Rule('G', "GG")
                ),
                8, 10, 0.01f);
    }
}
