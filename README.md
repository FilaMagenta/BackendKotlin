# Filà Magenta Backend

[![Last Version](https://img.shields.io/docker/v/arnyminerz/filamagenta?style=for-the-badge)][docker-hub]
![Image Size](https://img.shields.io/docker/image-size/arnyminerz/filamagenta?style=for-the-badge)

## Environment variables

There are some environment variables that can be used for configuring the installation.

| Variable          | Description                                                                                        |
|-------------------|----------------------------------------------------------------------------------------------------|
| `JWT_SECRET`      | The secret to use for JWT authentication                                                           |
| `JWT_PRIVATE_KEY` | The private key to use for JWT auth                                                                |
| `JWT_ISSUER`      | The JWT issuer. Usually should not be overwritten, since it's handled by the server automatically. |
| `JWT_AUDIENCE`    | The JWT audience. Really not used, but it's good to set.                                           |                                                                                  |
| `JWT_REALM`       | The realm of the JWT token. Usually not overwritten.                                               |
| `CUSTOM_CERT`     | Set to `true` for specifying custom certs. A volume must be configured before.                     |

## Volumes
### Certificates
If you want to set your own certificates for production, make sure to set the environment variable `CUSTOM_CERT` to true,
and create a volume linking to `/config`, which is a folder that contains a file called `jwks.json`. See the
[development file](./src/main/resources/certs/jwks.json).

[docker-hub]: https://hub.docker.com/repository/docker/arnyminerz/filamagenta/general