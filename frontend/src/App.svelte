<script lang="ts">
    import { auth_token, auth_username } from "./stores/auth";

    import { onMount } from "svelte";
    import { fade } from "svelte/transition";

    import { User, RefreshCcw } from 'lucide-svelte'; // Login icon

    import EpigramCard from './lib/EpigramCard.svelte';
    import EpigramPopup from './lib/EpigramPopup.svelte';
    import LoginPopup from './lib/LoginPopup.svelte';

    import type { Epigram } from "./types";

    let epigram: Epigram | null;           // Currently displayed epigram 
    let showCreatePopup = false;    // If true, shows the create Epigram popup component 
    let showLoginPopup = false;     // If true, shows the login/register popup component

    async function getRandomEpigram() {
        epigram = null;
        await new Promise(r => setTimeout(r, 700));

        const res = await fetch("http://localhost:8080/api/epigram");

        if (res.ok && res.status !== 204) {  // make sure content is retrieved
            epigram = await res.json();
        }

    }

    let choice = 3;
    let interval: number;

    function startInterval() {
        if (interval) clearInterval(interval); // Clear previous interval if it existed
        
        // Start new interval, based on choice value
        if (choice > 0) {
            interval = setInterval(() => {
                getRandomEpigram();
        }, choice * 1000)
        }

    }

    onMount(() => {
        // Trigger startInterval on mount to initialize random epigram loading with initial delay 
        startInterval();
    });

    function handleClickLoginButton() {
        if (!!$auth_token|| !!$auth_username) {
            auth_token.set(null);
            auth_username.set(null);
        } else {
            showLoginPopup = true;
        }

    }

    function handleClickRefreshEpigramButton() {
        getRandomEpigram();
        startInterval(); // Start interval again to cancel the previous ongoing interval
    }





</script>

<main>

    <h1>Epigrams</h1>

    <div class="epigram-container">
        {#if epigram}
            <div transition:fade={{duration: 500}}>
                <EpigramCard epigram={epigram}/>
            </div>

        {/if}
        <div class="epigram-options">
            <button>
                <RefreshCcw size={24} onclick={handleClickRefreshEpigramButton}/>
            </button>
            <select bind:value={choice} onchange={startInterval} class="epigram-select-time">
                <option value={3}>every 3 seconds</option>
                <option value={5}>every 5 seconds</option>
                <option value={10}>every 10 seconds</option>
                <option value={30}>every thirty seconds</option>
                <option value={60}>every minute</option>
                <option value={-1}>never</option>
            </select>

        </div>


    </div>




    <button onclick={handleClickLoginButton} class="login-btn">
        <User size={24} />
        
        {#if $auth_username}
            <span class="token-text">{$auth_username}</span>
        {/if}
    </button>
        
    <span title={!$auth_token ? "You must be logged in to post" : ""}>
        <button 
            onclick={() => showCreatePopup = true}
            disabled={!$auth_token} 
            >
            Add Epigram
        </button>
    </span>



    <EpigramPopup open={showCreatePopup} onClose={() => showCreatePopup = false}/>

    <LoginPopup open={showLoginPopup} onClose={() => showLoginPopup = false}/>


</main>

<style>

    .login-btn {
        display: flex;
        flex-direction: row;
        align-items: flex-end;
        position: fixed;
        top: 1rem;
        right: 1rem;
        background: transparent;
    }

    .token-text {
        font-size: 24px;
        margin-left: 0.5rem;
        
    }

    .epigram-container {
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        height: 10rem;
        width: 50rem;
        border: 1px solid transparent;
        border-radius: 8px;
    }

    .epigram-options {
        display: flex;
        flex-direction: row;
        position: absolute;
        right: 0;
        background: none;
        gap: 0.6rem;
    }

    .epigram-select-time {
        border: 1px solid transparent;
        border-radius: 8px;
        background: none;
    }

    select {
        border-radius: 8px;
        padding: 0.4rem 0.3rem;
        margin-left: -0.6rem;
        background: #222;
        /* border: 1px solid transparent; */
        font: inherit;
    }

    select:hover {
        border-color: #646cff;
    }

    option {
        color: black;          /* text color */
        background: white;     /* background */
    }



</style>
