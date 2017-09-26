/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aduer.calculator_test;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CalculatorExpressionTokenizer {

    private final Map<String, String> mReplacementMap;

    public CalculatorExpressionTokenizer(Context context) {
        mReplacementMap = new HashMap<>();

//        Locale locale = context.getResources().getConfiguration().locale;
//        if (!context.getResources().getBoolean(R.bool.use_localized_digits)) {
//            locale = new Locale.Builder()
//                .setLocale(locale)
//                .setUnicodeLocaleKeyword("nu", "latn")
//                .build();
//        }
//
//        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
//        final char zeroDigit = symbols.getZeroDigit();

        final char point = '.';
        final char zeroDigit = '0';

        mReplacementMap.put(".", String.valueOf(point));

        for (int i = 0; i <= 9; ++i) {
            mReplacementMap.put(Integer.toString(i), String.valueOf((char) (i + zeroDigit)));
        }

        mReplacementMap.put("/", context.getString(R.string.op_div));
        mReplacementMap.put("*", context.getString(R.string.op_mul));
        mReplacementMap.put("-", context.getString(R.string.op_sub));

        mReplacementMap.put("cos", context.getString(R.string.fun_cos));
        mReplacementMap.put("ln", context.getString(R.string.fun_ln));
        mReplacementMap.put("log", context.getString(R.string.fun_log));
        mReplacementMap.put("sin", context.getString(R.string.fun_sin));
        mReplacementMap.put("tan", context.getString(R.string.fun_tan));

        mReplacementMap.put("Infinity", context.getString(R.string.inf));
    }

    public String getNormalizedExpression(String expr) {
        for (Entry<String, String> replacementEntry : mReplacementMap.entrySet()) {
            expr = expr.replace(replacementEntry.getValue(), replacementEntry.getKey());
        }
        return expr;
    }

    public String getLocalizedExpression(String expr) {
        for (Entry<String, String> replacementEntry : mReplacementMap.entrySet()) {
            expr = expr.replace(replacementEntry.getKey(), replacementEntry.getValue());
        }
        return expr;
    }
}
