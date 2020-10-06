# Java Utils

Library for user's payload encryption used with Sitejabber.


## Installation

You can clone the repository or extract the [ZIP](https://github.com/sitejabber/java-utils/archive/master.zip).
Compile the class: `javac Utils.java`
Create the package: `javac -d . Utils.java`
Then have to add a `import sitejabber.Utils;` line.


## Usage

- Get your API keys on https://biz.sitejabber.com/account
- JSON encode your user data
- Call the encrypt method
- URL-encode the result
- Redirect the user to the feedback link

```
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONValue;
import sitejabber.Utils;

HashMap<String, String> userData = new HashMap<String, String>();
userData.put("email", "janedoe@gmail.com");
userData.put("order_date", "06-13-2013");
userData.put("order_id", "1234");
userData.put("first_name", "Jane");
userData.put("last_name", "Doe");

String json = JSONValue.toJSONString(userData);
Utils sitejabber = new Utils(); 
String encryptedData = sitejabber.encrypt(json, API_SECRET);
String feedbackLink = "https://www.sitejabber.com/biz-review?key=API_KEY&payload=" + URLEncoder.encode(encryptedData, StandardCharsets.UTF_8.toString());
```
