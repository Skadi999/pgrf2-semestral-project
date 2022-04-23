package project.generators;

import project.Rule;

import java.util.List;

public class Bush extends Generator{
    public Bush() {
        super(90, 25, 1f, 3f, "Y",
                List.of(
                        new Rule('X', "X[-FFF][+FFF]FX"),
                        new Rule('Y', "YFX[+Y][-Y]")
                ),
                10);
    }
}
