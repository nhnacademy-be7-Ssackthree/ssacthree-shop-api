package com.nhnacademy.ssacthree_shop_api.memberset.address.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.memberset.address.controller.AddressController;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressCreateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.address.dto.AddressResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.address.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AddressController(addressService)).build();
    }

    @Test
    void testCreateAddress_Success() throws Exception {
        AddressCreateRequest request = new AddressCreateRequest("alias", "roadname", "detail", "postalNumber");
        AddressResponse response = new AddressResponse(1L, "alias", "roadname", "detail", "postalNumber");

        when(addressService.createAddress(eq("testUserId"), any(AddressCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/shop/members/address")
                .header("X-USER-ID", "testUserId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.addressAlias").value("alias"))
            .andExpect(jsonPath("$.addressRoadname").value("roadname"));
    }

    @Test
    void testGetAllAddresses_Success() throws Exception {
        AddressResponse addressResponse = new AddressResponse(1L, "alias", "roadname", "detail", "postalNumber");

        when(addressService.getAddressesByUserId("testUserId")).thenReturn(List.of(addressResponse));

        mockMvc.perform(get("/api/shop/members/address")
                .header("X-USER-ID", "testUserId"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].addressAlias").value("alias"))
            .andExpect(jsonPath("$[0].addressRoadname").value("roadname")) // 이 부분이 'roadname'이 되는지 확인
            .andExpect(jsonPath("$[0].addressDetail").value("detail"))
            .andExpect(jsonPath("$[0].addressPostalNumber").value("postalNumber"));
    }

    @Test
    void testDeleteAddress_Success() throws Exception {
        doNothing().when(addressService).deleteAddressById("1");

        mockMvc.perform(delete("/api/shop/members/address/1"))
            .andExpect(status().isNoContent());
    }
}