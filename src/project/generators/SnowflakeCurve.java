package project.generators;

import project.Rule;

import java.util.List;

public class SnowflakeCurve extends Generator{
    public SnowflakeCurve() {
        super( 90, 60, 1f, 3f, "F++F++F",
                List.of(
                        new Rule('F', "F-F++F-F")
                ),
                6);
    }
}
