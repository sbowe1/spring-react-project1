import Box from '@mui/material/Box';
import Image from 'next/image';
import logoArtPlaceholder from '../../ui/temp/logo-art-placeholder-and-or-promo-copy.jpg';
import { RegisterForm } from "./register-form";

export default function Register() {
    return (
        <Box sx={{ display: 'flex', justifyContent: 'space-around'}}>
            <RegisterForm />
            <Image src={logoArtPlaceholder} alt="Logo Art Placeholder" width={529} height={582} />
        </Box>
    );
  }

