package project.generators;

import project.Rule;

import java.util.List;

public class QuadraticSnowflakeVariant extends Generator{
    public QuadraticSnowflakeVariant() {
        super(90, 90, 1f, 3f, "FF+FF+FF+FF",
                List.of(
                        new Rule('F', "F+F-F-F+F")
                ),
                5);
    }
}
