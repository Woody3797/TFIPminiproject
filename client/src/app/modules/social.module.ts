import { GoogleLoginProvider, SocialLoginModule, SocialAuthServiceConfig } from "@abacritt/angularx-social-login";

export const SocialAuthServiceProvider = [
    {
        provide: 'SocialAuthServiceConfig',
        useValue: {
            autoLogin: false,
            providers: [
                {
                    id: GoogleLoginProvider.PROVIDER_ID,
                    provider: new GoogleLoginProvider('216028312066-dll1brg9sq28s4lmi1vsi1n0u9gvptoa.apps.googleusercontent.com')
                }
            ],
            onError: (err: any) => {
                console.error(err);
            }
        } as SocialAuthServiceConfig
    }
]