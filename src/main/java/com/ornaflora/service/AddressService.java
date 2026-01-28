package com.ornaflora.service;

import com.ornaflora.dto.AddressDTO;
import com.ornaflora.dto.AddressRequest;
import com.ornaflora.model.Address;
import com.ornaflora.model.User;
import com.ornaflora.repository.AddressRepository;
import com.ornaflora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressDTO saveAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If this is the first address or marked as default, set it as default
        List<Address> userAddresses = addressRepository.findByUserId(userId);
        boolean isDefault = request.getIsDefault() != null ? request.getIsDefault() : userAddresses.isEmpty();

        if (isDefault) {
            // Unset default for other addresses
            userAddresses.stream()
                    .filter(Address::getIsDefault)
                    .forEach(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
        }

        Address address = Address.builder()
                .user(user)
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .phone(request.getPhone())
                .isDefault(isDefault)
                .build();

        Address savedAddress = addressRepository.save(address);
        return convertToDTO(savedAddress);
    }

    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return convertToDTO(address);
    }

    public List<AddressDTO> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AddressDTO getDefaultAddress(Long userId) {
        Address address = addressRepository.findDefaultAddressByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No default address found"));
        return convertToDTO(address);
    }

    public AddressDTO updateAddress(Long id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setPhone(request.getPhone());

        Address updatedAddress = addressRepository.save(address);
        return convertToDTO(updatedAddress);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    public AddressDTO setDefaultAddress(Long userId, Long addressId) {
        // Verify address belongs to user
        if (!addressRepository.existsByIdAndUserId(addressId, userId)) {
            throw new RuntimeException("Address not found for user");
        }

        // Unset default for all other addresses
        addressRepository.findByUserId(userId).stream()
                .filter(Address::getIsDefault)
                .forEach(addr -> {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                });

        // Set this address as default
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);

        return convertToDTO(updatedAddress);
    }

    private AddressDTO convertToDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .phone(address.getPhone())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .build();
    }
}
