package valoon.loader;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoaderController {

    private LoaderService loaderService;

    public LoaderController(LoaderService loaderService) {
        this.loaderService = loaderService;
    }

    /**
     * Fill the database with the information stored into the resource folder
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/loader",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> createInformation() {
        loaderService.createInformation();
        String json = "{\"response\": \"OK\"}";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
