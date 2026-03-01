<script lang="ts">
    import { auth_token, auth_username } from "../stores/auth";

    let { open, onClose } = $props();
    let dialogElement: HTMLDialogElement | null = null;

    let username = $state("");
    let password = $state("");

    let usernameError: String | null = $state(null);
    let passwordError: String | null = $state(null);


    $effect(() => {
        if (!dialogElement) return;

        if (open && !dialogElement.open) {
            dialogElement.showModal();
        } else if (!open && dialogElement.open) {
            dialogElement.close();
        }
    });

    function handleClose() {
        username = "";
        password = "";
        usernameError = null;
        passwordError = null;

        onClose();
    }

    async function register() {
        
        const res = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        // Prevent double error
        usernameError = null;
        passwordError = null;
        
        const data = await res.json();

        if (res.ok) {
            auth_token.set(data.token);       // Set token in store
            auth_username.set(data.username); // Set authenticated username in store

            handleClose();
        } else {
            console.error("Registration failed");
            if (data.status == 400) {
                if (data.errorCode == "INVALID_USERNAME") {
                    usernameError = data.message;
                } else if (data.errorCode == "INVALID_PASSWORD") {
                    passwordError = data.message;
                }
            }
            if (data.status == 409) {
                usernameError = data.message;
            }
        }
    }
    
    async function login() {
        
        const res = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });
        
        // Prevent double error
        usernameError = null;
        passwordError = null;
        
        const data = await res.json();

        if (res.ok) {
            auth_token.set(data.token);       // Set token in store
            auth_username.set(data.username); // Set authenticated username in store

            handleClose();
        } else {
            console.error("Login failed");
            if (data.status == 401) {
                passwordError = "Incorrect password";
            }
            if (data.status == 404) {
                usernameError = "Username not found"
            }
        }

    }

</script>

<dialog bind:this={dialogElement} onclose={handleClose}>
    <h3>Log in, or register an account</h3>
    <div class="text-field-wrapper">
        <div class="input-wrapper">
            <input 
                type="text"
                maxLength="255"
                placeholder="Username"
                class="normal-input"
                class:invalid={!!usernameError}
                bind:value={username}
                oninput={() => usernameError = null}
            />
            {#if !!usernameError}
                <p class="error">{usernameError}</p>
            {/if}
            
        </div>
        <div class="input-wrapper">
            <input
                type="password"
                maxLength="255"
                placeholder="Password"
                class="normal-input"
                class:invalid={!!passwordError}
                bind:value={password}
                oninput={(() => passwordError = null)}
            />
            {#if !!passwordError}
                <p class="error">{passwordError}</p>
            {/if}
        </div>

        <button
            type="button"
            class="login-btn"
            onclick={login}
        >
        Login
        </button>



    </div>

    <button
        type="button"
        class="register-btn"
        onclick={register}
    >
    Register
    </button>



    <button 
        class="close-btn" 
        onclick={onClose} 
        aria-label="Close popup">
        Close
        </button>
</dialog>


<style>
.login-btn {
    font-size: 1rem;      
    padding: 0.5rem 1rem;        
    line-height: 1rem;          
    border-radius: 8px;
    border: none;
    background-color: blue;
    color: white;
    cursor: pointer;
}
.register-btn {
    margin-top: 1rem;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 8px;
    border: 1px solid grey;
    color: grey;
    cursor: pointer;
}
.register-btn:hover,
.register-btn:focus {
    background-color: grey;
    color: black;
    outline: none;
}


.login-btn:hover,
.login-btn:focus {
    background-color: darkblue;
    outline: none;
}
.invalid {
    border-color: red;
    background-color: #ffe6e6;
}

.text-field-wrapper {
    display: flex;
    flex-direction: row;
    gap: 0.5rem;
    padding: 1rem;
    margin-bottom: 1rem;

}

.input-wrapper {
    position: relative; 
    display: flex;
    flex-direction: column;
    width: 45%;
}


.error {
    position: absolute;
    left: 0.6rem; 
    bottom: -1.7rem;
    color: red;
    font-size: 0.875rem;
    height: 1em;      /* reserve space so input doesn't jump */
    margin-top: 0.25rem;
}
</style>
