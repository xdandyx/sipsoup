package com.virjar.sipsoup.function.filter;

import java.util.List;

import org.jsoup.nodes.Element;

import com.virjar.sipsoup.parse.expression.SyntaxNode;

/**
 * Created by virjar on 17/6/15.
 */
public class LowerCaseFunction extends AbStractStringFunction {
    @Override
    public Object call(Element element, List<SyntaxNode> params) {
        return firstParamToString(element, params).toLowerCase();
    }

    @Override
    public String getName() {
        return "lower-case";
    }
}
