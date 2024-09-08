package com.mg.gra.application.parser;

import java.io.InputStream;
import java.util.List;

import com.mg.gra.application.dto.MovieData;

public interface MovieDataParser {

    List<MovieData> parse(InputStream fileInputStream);
}
