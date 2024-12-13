package fa.training.vivuspringboot.services;

import fa.training.vivuspringboot.dtos.auth.RegisterRequestDTO;

public interface IAuthService {
    boolean register(RegisterRequestDTO registerDTO);
}
