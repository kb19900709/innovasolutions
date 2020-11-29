package com.innova.service.impl;

import com.innova.service.ValidationService;
import com.innova.service.constant.ValidationErrorEnum;
import com.innova.service.exception.PasswordException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordValidationService implements ValidationService<String> {

    @Override
    public void validate(String password) {
        validateLength(password);
        validateCharacter(password);
        validateSequence(password);
    }

    /**
     * Assume the password is between 5 and 12 characters in length. If the password is unfit for the condition, then throw
     * {@link PasswordException}.
     *
     * @param password the string is a password
     * @throws PasswordException if the length of the password isn't qualified
     */
    protected void validateLength(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new PasswordException(ValidationErrorEnum.PASSWORD_INVALID_LENGTH);
        }

        int passwordLength = password.length();
        if (passwordLength < 5 || passwordLength > 12) {
            throw new PasswordException(ValidationErrorEnum.PASSWORD_INVALID_LENGTH);
        }
    }

    /**
     * Assume the password consists of a mixture of lowercase letters and numerical digits only. If the password is
     * unfit for the condition, then throw {@link PasswordException}.
     *
     * @param password the string is a password
     * @throws PasswordException if the password isn't a mixture of lowercase letters and numerical digits, with
     * at least one of each
     */
    protected void validateCharacter(String password) {
        boolean intChar = false;
        boolean letterChar = false;
        for (char asciiCode : password.toCharArray()) {
            // 0 to 9
            if (asciiCode >= 48 && asciiCode <= 57) {
                intChar = true;
            }

            // a to z
            if (asciiCode >= 97 && asciiCode <= 122) {
                letterChar = true;
            }
        }

        if (!(intChar && letterChar)) {
            throw new PasswordException(ValidationErrorEnum.PASSWORD_INVALID_CHARACTER);
        }
    }

    /**
     * Assume the password doesn't contain any sequence of characters immediately followed by the same sequence, If the
     * password is unfit for the condition, then throw {@link PasswordException}.
     *
     * @param password the string is a password
     * @throws PasswordException if the password contain any sequence of characters immediately followed by
     * the same sequence
     */
    protected void validateSequence(String password) {
        int targetLength = password.length();

        // get the sub string starts from index 0
        // a immediate sequence string which length must be more than or equal to 4
        // so if the index is equal to targetLength minus 3, then stop the loop
        for (int index = 0; index < targetLength - 3; index++) {
            String subString = password.substring(index);
            Optional<CharSequenceRecord> maxLpsValueOp = getCharSequenceRecord(subString)
                    .stream()
                    .max(Comparator.comparing(CharSequenceRecord::getLpsValue));

            if (!maxLpsValueOp.isPresent()) {
                continue;
            }

            CharSequenceRecord charSequenceRecord = maxLpsValueOp.get();
            int lpsIndex = charSequenceRecord.getLpsIndex();
            int lpsValue = charSequenceRecord.getLpsValue();
            if (lpsValue >= 2) {
                // for normal case
                // e.g. aabaab678,      lps[] = [0, 1, 0, 1, 2, 3, 0, 0, 0]
                //      aabbaabb123,    lps[] = [0, 1, 0, 0, 1, 2, 3, 4, 0, 0, 0]
                if (lpsValue * 2 == lpsIndex + 1) {
                    throw new PasswordException(ValidationErrorEnum.PASSWORD_INVALID_SEQUENCE);
                }

                // for duplicate letter case
                // e.g. aaaa123,        lps[]:[0, 1, 2, 3, 0, 0, 0]
                //      aaaaaa,         lps[]:[0, 1, 2, 3, 4, 5]
                if (lpsValue >= 3 && lpsIndex == lpsValue) {
                    throw new PasswordException(ValidationErrorEnum.PASSWORD_INVALID_SEQUENCE);
                }
            }
        }
    }

    private List<CharSequenceRecord> getCharSequenceRecord(String targetStr) {
        int length = targetStr.length();

        int[] lps = new int[length];
        lps[0] = 0; // index 0 would always be 0
        int i = 1;  // since the first element skipped, the string index starts from 0
        int len = 0;

        // follow KMP algorithm, get an int array which aims to find the longest prefix and suffix length
        while (i < targetStr.length()) {
            if (targetStr.charAt(i) == targetStr.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
                continue;
            }

            if (len != 0) {
                // in case of duplicated letter
                len = lps[len - 1];
                continue;
            }

            lps[i] = 0;
            i++;
        }

        List<CharSequenceRecord> result = new ArrayList<>();

        for (int lpsIndex = 0; lpsIndex < lps.length; lpsIndex++) {
            result.add(new CharSequenceRecord(lpsIndex, lps[lpsIndex]));
        }

        return result;
    }

    @Getter
    @Setter
    private class CharSequenceRecord {
        private int lpsIndex;
        private int lpsValue;

        public CharSequenceRecord(int lpsIndex, int lpsValue) {
            this.lpsIndex = lpsIndex;
            this.lpsValue = lpsValue;
        }
    }
}
