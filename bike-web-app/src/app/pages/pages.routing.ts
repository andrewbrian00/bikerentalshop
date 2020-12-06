import { LoginComponent } from './login/login.component';
import { Routes } from '@angular/router';

export const PagesRoutes: Routes = [

    {
        path: '',
        children: [ {
            path: 'login',
            component: LoginComponent
        }]
    }
];
