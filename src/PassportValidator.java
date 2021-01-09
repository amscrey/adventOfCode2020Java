import util.AdventFileHelper;
import util.NameValuePair;

import java.util.*;
import java.util.regex.Pattern;
//import org.apache.commons.lang3.tuple;

public class PassportValidator {

    public static void main(String[] args) {
        String rawdata = AdventFileHelper.readAll("/Users/dc24863/dev/supplychain/Advent2/passportData1.txt");
        //String rawdata = util.AdventFileHelper.readALl("/Users/dc24863/dev/supplychain/Advent2/passportData_TEST_INVALID.txt");
        //String rawdata = util.AdventFileHelper.readALl("/Users/dc24863/dev/supplychain/Advent2/passportData_TEST_VALID.txt");
        List<AdventFileHelper.RawDataSegment> psegs = AdventFileHelper.parseRawDataSegments(rawdata);
        List<Passport> passports = parsePassports(psegs);
        long validCount = countValidPassports(passports, new AttributeValidator());
        System.out.println("Valid Count: " + validCount);
    }

    static List<Passport> parsePassports(List<AdventFileHelper.RawDataSegment> segments) {
        List<Passport> passports = new ArrayList<>();
        for (AdventFileHelper.RawDataSegment segment : segments) {
            passports.add(new Passport(segment));
        }
        return passports;
    }

    static long countValidPassports(List<Passport> passports, AttributeValidator validator) {
        long valid = 0;
        int count = 0;
        for (Passport passport : passports) {
            count++;
            boolean isValid = passport.isValid(validator);
            if (isValid) { valid++;
                ////System.out.println("Valid Passport: " + count + " -> " + passport);
            }
            ///else {System.out.println("!invalid Passport!: "  + count + " -> " + passport);}
        }
        return valid;
    }


    public static class Passport {
        List<NameValuePair> attributes;
        Map<String, NameValuePair> attMap;

        public Passport(AdventFileHelper.RawDataSegment segment) {
            this.attributes = parseAttributes(segment);
            this.attMap = new Hashtable<>();
            for (NameValuePair attribute : this.attributes) {
              this.attMap.put(attribute.getName(), attribute);
            }
        }

        boolean isValid(AttributeValidator validator) {
            boolean valid = true;
            for (String reqAttName : validator.getRequiredAttributes()) {
                NameValuePair attFound = attMap.get(reqAttName);
                valid = validator.validate(attFound);
                if (!valid) {
                    System.out.println("InValid !!! Bad Attribute: " + reqAttName);
                    break;
                }
            }
            return valid;
        }



        static List<NameValuePair> parseAttributes(AdventFileHelper.RawDataSegment segment) {
            List<NameValuePair> attList = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(segment.getRawData());
            while (st.hasMoreTokens()) {
                String rawAttrData = st.nextToken();
                NameValuePair nvp = parseRawAtrribute(rawAttrData);
                attList.add(nvp);
            }
            return attList;
        }

        static NameValuePair parseRawAtrribute(String rawAtt) {
            StringTokenizer st = new StringTokenizer(rawAtt, ":");
            String name = st.nextToken();
            String value = st.nextToken();
            return new NameValuePair(name, value);
        }

        @Override
        public String toString() {
            return "Passport{" +
                    "attributes=" + attributes +
                    '}';
        }
    }

    static enum EYECOLOR {amb, blu, brn, gry, grn, hzl, oth}

    static class AttributeValidator {

        public static final String BYR = "byr";
        public static final String IYR = "iyr";
        public static final String EYR = "eyr";
        public static final String HGT = "hgt";
        public static final String HCL = "hcl";
        public static final String ECL = "ecl";
        public static final String PID = "pid";

        List<String> getRequiredAttributes() {
            List<String> reqAtts = new ArrayList<>();
            reqAtts.add(BYR);
            reqAtts.add(IYR);
            reqAtts.add(EYR);
            reqAtts.add(HGT);
            reqAtts.add(HCL);
            reqAtts.add(ECL);
            reqAtts.add(PID);
            return reqAtts;
        }

         boolean validate(NameValuePair attribute) {
            if (attribute == null) return false;
            switch (attribute.getName()) {
                case BYR:
                    return validateByr(attribute.getValue());
                case IYR:
                    return validateIyr(attribute.getValue());
                case EYR:
                    return validateEyr(attribute.getValue());
                case HGT:
                    return validateHgt(attribute.getValue());
                case HCL:
                    return validateHcl(attribute.getValue());
                case ECL:
                    return validateEcl(attribute.getValue());
                case PID:
                    return validatePid(attribute.getValue());
                default:
                    return true;
            }
        }

        static boolean validateYear(String value, int lowerLim, int upperLim) {
            // byr (Birth Year) - four digits; at least 1920 and at most 2002.
            if (value.length() < 4) return false;
            try {
                int year = Integer.parseInt(value);
                if ((lowerLim > year) || (upperLim < year)) return false;
            } catch (NumberFormatException nfe) {return false;}
            return true;
        }
        static boolean validateByr(String value) {
            // byr (Birth Year) - four digits; at least 1920 and at most 2002.
            return validateYear(value, 1920,2002);
        }

        static boolean validateIyr(String value) {
            // iyr (Issue Year) - four digits; at least 2010 and at most 2020.
            return validateYear(value, 2010,2020);
        }
        static boolean validateEyr(String value) {
            //eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
            return validateYear(value, 2020,2030);
        }
        static boolean validateHgt(String value) {
            // hgt (Height) - a number followed by either cm or in:
            // If cm, the number must be at least 150 and at most 193.
            // If in, the number must be at least 59 and at most 76.
            String regex = "^[1-9][0-9]*(cm|in)$";
            boolean match = Pattern.matches(regex, value);
            if (!match) return false;
            String quantityString = value.substring(0,value.length()-2);
            int quantity = Integer.parseInt(quantityString);
            String unitType = value.substring(value.length()-2, value.length());
            if ("cm".equals(unitType)) {
                if ((quantity < 150) || (quantity > 193)) return false;
            } else {
                if ((quantity < 59) || (quantity > 76)) return false;
            }
            return true;
        }

        static boolean validateHcl(String value) {
            // hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
            String regex = "^#[0-9a-f]{6}$";
            boolean match = Pattern.matches(regex, value);
            return match;
        }

        static boolean validateEcl(String value) {
            // ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
            try {
                EYECOLOR ec = EYECOLOR.valueOf(value);
            } catch (IllegalArgumentException iae) {return false;}
            return true;
        }

        static boolean validatePid(String value) {
            // pid (Passport ID) - a nine-digit number, including leading zeroes.
            if (value.length() != 9) return false;
            try {
                int asInt = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {return false;}
            return true;
        }
    }

}
