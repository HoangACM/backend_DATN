package code.service.customer;

import code.exception.BadRequestException;
import code.model.entity.Address;
import code.model.entity.User;
import code.exception.NotFoundException;
import code.model.request.CreateAddressRequest;
import code.repository.AddressRepository;
import code.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  private AddressRepository addressRepository;
  private UserRepository userRepository;

  private AddressService(AddressRepository addressRepository,UserRepository userRepository) {
    this.addressRepository = addressRepository;
    this.userRepository = userRepository;
  }

// Lấy tất cả Address của 1 người dùng theo userId
  public List<Address> getAddressesByUserId(User user){
    return this.addressRepository.findByUser(user);
  }

//  Lấy Address cụ thể của 1 người dùng theo userId và addressId
  public Address getAddressById(User user,long addressId){
    Address address = addressRepository.findById(addressId)
        .orElseThrow(()-> new NotFoundException("Không thấy Address có id : "+addressId));
    if(address.getUser().getId() != user.getId()){
      throw new NotFoundException("Không tìm thấy Address phù hợp");
    }
    return address;
  }
  public Address createAddress(User user, CreateAddressRequest request){
    Address address = new Address();
    BeanUtils.copyProperties(request,address);
    if(addressRepository.findByUser(user).size() == 0){
      address.setDefaultAddress(true);
    }
    else{
      if(request.isDefaultAddress() == true){
        Address addressDefault = addressRepository.findByDefaultAddress(true);
        addressDefault.setDefaultAddress(false);
        addressRepository.save(addressDefault);
      }
      else{
        address.setDefaultAddress(false);
      }
    }
    address.setUser(user);
    return addressRepository.save(address);
  }

//  Cập nhật lại địa chỉ mới theo addressId
public Address updateAddress(User user,long addressId, CreateAddressRequest request){
  Address address = addressRepository.findById(addressId)
      .orElseThrow(()-> new NotFoundException("Không thấy Address có id : "+addressId));
  if(address.getUser().getId() != user.getId()){
    throw new BadRequestException("Không thực hiện được yêu cầu này");
  }
  if(request.isDefaultAddress() == true){
    Address addressDefault = addressRepository.findByDefaultAddress(true);
    addressDefault.setDefaultAddress(false);
    addressRepository.save(addressDefault);
    BeanUtils.copyProperties(request,address);
  }
  else{
    BeanUtils.copyProperties(request,address);
  }
  return addressRepository.save(address);
}
}
