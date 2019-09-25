package prax2;

class JsonParser {


    public static String deleteOrderId(String jsonString) {
        String idKey = "\"id\"";
        int index = 1;
        int child = 0;
        while(index > 0) {
            int start = jsonString.indexOf('\"', index);
            //System.out.println(jsonString.substring(start, idKey.length() + start));
            if (jsonString.substring(start, idKey.length() + start).equals(idKey)) {
                int end = skipNameIndex(index,jsonString) + 1;
                end = skipChildIndex(index, jsonString);
                // System.out.println(end);
                if (child == 0) {
                    if (jsonString.indexOf(',', end) > 0) {
                        end = jsonString.indexOf(',', end) + 1;
                    }
                    jsonString =  jsonString.substring(0, start) + jsonString.substring(end);
                    continue;
                } else {
                    jsonString =  jsonString.substring(0, index - 1) + jsonString.substring(end);
                }
            } else {
                index = skipNameIndex(index, jsonString) + 1;
                index = skipChildIndex(index, jsonString);
                child += 1;
            }
            index = jsonString.indexOf(',', index) + 1;
        }
        return jsonString;
    }

     private static int skipChildIndex(int index, String jsonString) {
        int counter = jsonString.indexOf(":", index) + 1;
        while(Character.isSpaceChar(jsonString.charAt(counter))) { counter++;}
        if (jsonString.charAt(counter) =='{' || jsonString.charAt(counter) =='[') {
            counter = skipObjectIndex(counter, jsonString);
        } else {
            while(Character.isDigit(jsonString.charAt(counter))
                    || Character.isLetter(jsonString.charAt(counter))
                    || jsonString.charAt(counter) =='.') {counter++;}
        }
        return counter;
    }

    private static int skipObjectIndex(int index, String jsonString) {
        int stock = 1;
        index++;
        while (stock > 0) {
            // System.out.println(stock + " " + counter + " : " + jsonString.charAt(counter));
            if (jsonString.charAt(index) =='{' || jsonString.charAt(index) =='[') {stock++;}
            if (jsonString.charAt(index) =='}' || jsonString.charAt(index) ==']') {stock--;}
            if (jsonString.charAt(index) == '\"') {index = skipNameIndex(index, jsonString);}
            index++;
        }
        return index;
    }

   public  static int skipNameIndex(int index, String jsonString) {
        int counter = index + 1;
        while(jsonString.charAt(counter) != '\"') {
            if (jsonString.charAt(counter) == '\\') {counter++;}
            counter++;
        }
        return counter;
    }
}
