"use client";
import Add from '@mui/icons-material/Add';
import ContactSupportOutlined from '@mui/icons-material/ContactSupportOutlined';
import Logout from '@mui/icons-material/Logout';
import Person from '@mui/icons-material/Person';
import Settings from '@mui/icons-material/Settings';
import Speed from '@mui/icons-material/Speed';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import ListItemIcon from '@mui/material/ListItemIcon';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import Image from 'next/image';
import { useState } from 'react';
import logoPlaceholder from './temp/logo-placeholder.jpg';

export default function MenuBar() {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const open = Boolean(anchorEl);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => setAnchorEl(null);

  return (
    <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center', justifyContent: 'space-between' }}>

      <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center', gap: '8px', width: '249px', height: '75px'}}>
        <Image src={logoPlaceholder} alt="Logo Placeholder" width={75} height={75} />
        <Typography>Site Name</Typography>
      </Box>

      <Button variant="contained" sx={{backgroundColor: '#65558F', width: '149px', height: '40px'}}><Typography>Resume Course</Typography></Button>

      <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center' }}>
        <Button size='large' startIcon={<Speed/>}><Typography>Dashboard</Typography></Button>
        <Button size='large' startIcon={<ContactSupportOutlined/>}><Typography>Quick Quiz</Typography></Button>
        <Button size='large' startIcon={<Add/>}><Typography>Builder</Typography></Button>
        <Tooltip title="Account settings">
          <Button
            startIcon={<Person/>}
            onClick={handleClick}
            size="large"
            aria-controls={open ? 'account-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={open ? 'true' : undefined}
          >
            <Typography>Account</Typography>
          </Button>
        </Tooltip>
      </Box>

      <Menu
        anchorEl={anchorEl}
        id="account-menu"
        open={open}
        onClose={handleClose}
        onClick={handleClose}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        <MenuItem onClick={handleClose}>
          <ListItemIcon>
            <Settings fontSize="small" />
          </ListItemIcon>
          <Typography>Settings</Typography>
        </MenuItem>

        <MenuItem onClick={handleClose}>
          <ListItemIcon>
            <Logout fontSize="small" />
          </ListItemIcon>
          <Typography>Logout</Typography>
        </MenuItem>

      </Menu>

    </Box>
  );
}