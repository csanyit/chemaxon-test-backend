package com.chemaxon.homework.csanyit.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChemicalizeTestHelper {

    public static final List<Map<String, Object>> MOCK_DESCRIPTION_SUMMARY = new ArrayList<>();

    static {
        final Map<String, Object> molar_mass_map = new HashMap<>();
        molar_mass_map.put("group", "BASIC");
        molar_mass_map.put("key", "molar_mass");
        molar_mass_map.put("label", "Molar mass [g/mol]");
        molar_mass_map.put("value", 180.159);
        final Map<String, Object> exact_mass_map = new HashMap<>();
        exact_mass_map.put("group", "BASIC");
        exact_mass_map.put("key", "exact_mass");
        exact_mass_map.put("label", "Exact mass [Da]");
        exact_mass_map.put("value", 180.042258738);
        final Map<String, Object> formula_map = new HashMap<>();
        formula_map.put("group", "BASIC");
        formula_map.put("key", "formula");
        formula_map.put("label", "Formula");
        formula_map.put("value", "C9H8O4");
        final Map<String, Object> composition_map = new HashMap<>();
        composition_map.put("group", "BASIC");
        composition_map.put("key", "composition");
        composition_map.put("label", "Composition");
        composition_map.put("value", "C (60%), H (4.48%), O (35.52%)");
        MOCK_DESCRIPTION_SUMMARY.add(molar_mass_map);
        MOCK_DESCRIPTION_SUMMARY.add(exact_mass_map);
        MOCK_DESCRIPTION_SUMMARY.add(formula_map);
        MOCK_DESCRIPTION_SUMMARY.add(composition_map);
    }

}
