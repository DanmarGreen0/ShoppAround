package com.online_shopping_rest_api.integrationTest;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

//    @Autowired
//    public TestRestTemplate restTemplate;
//
//   @Test
//    public void getMasterUserAccount() throws IllegalArgumentException, IOException {
//       ResponseEntity<String> response = restTemplate.withBasicAuth("danmargreen","Terren17").getForEntity("/user/4", String.class);
//       String[] rb_fields = response.getBody().split("([\\[\\]{}]|^(, )|([\"],[\"]|\"))");
//       Map<String,String> mapped_rb_fields = new LinkedHashMap<>();
//
//       System.out.print("\n<--------------------------------------TestUserControllerTest-------------------------------------->\n");
//
//        for(int i = 0; i < rb_fields.length; i++){
//            if(rb_fields[i].equals(":")){
//                mapped_rb_fields.put(rb_fields[i-1],rb_fields[i+1]);
//            }
//        }
//        /*
//            To do:
//                1) print roles list
//         */
//       for (Map.Entry<String,String> field: mapped_rb_fields.entrySet()) {
//           System.out.print(field.getKey() + ": " + field.getValue() + "\n");
//       }
//       assertThat(mapped_rb_fields.get("username")).isEqualTo("danmargreen");
//       assertThat(mapped_rb_fields.get("firstName")).isEqualTo("Danmar");
//       assertThat(mapped_rb_fields.get("lastName")).isEqualTo("Green");
//       assertThat(BCrypt.checkpw("Terren17", mapped_rb_fields.get("password"))).isTrue();
//       assertThat(mapped_rb_fields.get("dateOfBirth")).isEqualTo("1996-17-01");
//       assertThat(mapped_rb_fields.get("email")).isEqualTo("danmargreen0@gmail.com");
//       assertThat(mapped_rb_fields.get("address")).isEqualTo("5955 Skyline Dr, West Linn, OR, 97068");
//       assertThat(mapped_rb_fields.get("phoneNo")).isEqualTo("(503)-833-2453");
//
//       System.out.print("\n<--------------------------------------Ends-------------------------------------->\n");
//   }

}
