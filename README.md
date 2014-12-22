### Daniel Fitzpatrick's BouncyCastle code

This is a fork of BouncyCastle (https://github.com/bcgit/bc-java) to add more algorithms to the post-quantum crypto package.

#### Isogeny

An implementation of the isogeny-based key agreement from 

* D. Jao and L. De Feo, Towards quantum-resistant cryptosystems from supersingluar elliptic curve isogenies, PQCrypto 2011.

The core elliptic curve arithmetic and isogeny calculations closely follow De Feo's original C/Sage code (https://github.com/defeo/ss-isogeny-software/).
