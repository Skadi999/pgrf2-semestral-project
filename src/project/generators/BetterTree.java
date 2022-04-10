package project.generators;

import project.Rule;

import java.util.List;

public class BetterTree extends Generator {

    public BetterTree() {
        super(-0.5f, -0.8f, 75, 20, 1f, 2.5f, "F",
                List.of(
                        new Rule('F', "G+[[F]-F]-G[-GF]+F"),
                        new Rule('G', "GG")
                ));
    }
}
//new Rule('F', "G+[[F]-F]-G[-GF]+F"),