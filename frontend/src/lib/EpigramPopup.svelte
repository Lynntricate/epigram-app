<script lang="ts">
    import { auth_token, auth_username } from "../stores/auth";
    import { get } from "svelte/store";


    let { open, onClose } = $props();
    let dialogElement: HTMLDialogElement | null = null;

    let content = $state("");
    let author = $state("");
    let mine = $state(false);


    $effect(() => {
        if (!dialogElement) return;

        if (open && !dialogElement.open) {
            dialogElement.showModal();
        } else if (!open && dialogElement.open) {
            dialogElement.close();
        }
    });

    function handleClose() {
        onClose();
    }


    async function submitEpigram() {
        const token = get(auth_token); // Get token from store

        if (!token) return alert("Not logged in"); // TODO cleaner implementation of this

        const epigram = { content, author, mine };

        try {
            console.log(get(auth_token));
            const response = await fetch("http://localhost:8080/api/epigrams", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(epigram)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const saved = await response.json();
            console.log("Saved epigram:", saved);

            // Clear form and close popup
            content = "";
            author = "";
            mine = false;
            onClose();

        } catch (err) {
            console.error("Failed to save epigram:", err);
        }
    }

</script>

<dialog bind:this={dialogElement} onclose={handleClose}>
    <form method="dialog" class="dialog-content">
        <h3>Add an Epigram</h3>

        <input
            type="text"
            maxlength="1000"
            placeholder="An epigram..."
            class="large-input"
            bind:value={content}
        />

        <div class="author-wrapper">
            <label>
                <input type="checkbox" bind:checked={mine} />
                Original
            </label>
            <div class="author-post-as-wrapper">
                <p style="display: flex; align-items: center; margin: 0">Author:  </p>
                {#if mine}
                    <strong>{$auth_username}</strong>
                {:else} 
                    <input
                        type="text"
                        maxlength="255"
                        placeholder="J.R.R. Tolkien"
                        class="author-input"
                        disabled={mine}
                        bind:value={author}
                    />
                {/if}

            </div>


        </div>

        <menu>
            <button type="submit" onclick={submitEpigram}>Submit</button>
            <button type="button" onclick={handleClose}>Close</button>
        </menu>
    </form>
</dialog>

<style>

    .large-input {
        font-size: 1.25rem;
        padding: 0.5rem;
        width: 100%;
        box-sizing: border-box;
        margin-bottom: 0.5rem;
        border-radius: 8px;
    }

    .dialog-content {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
    }



    .author-wrapper {
        display: flex;
        flex-direction: row;
        height: 2.5rem;
        align-items: center;
        gap: 5rem;
    }

    .author-post-as-wrapper {
        display: flex;
        flex-direction: row;
        gap: 0.4rem
    }

    .author-input {
        font-size: 1rem;
        padding: 0.5rem;
        box-sizing: border-box;
        border-radius: 8px;
        border-width: 1px;
    }

    menu {
        display: flex;
        justify-content: flex-end;
        gap: 0.5rem;
        margin-top: 1rem;
    }

    button {
        padding: 0.5rem 1rem;
        cursor: pointer;
    }
</style>