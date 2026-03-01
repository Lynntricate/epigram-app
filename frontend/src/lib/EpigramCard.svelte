<script lang="ts">
  export let epigram;


</script>


<div class="card">

    <strong class="epigram">{epigram.content}</strong><br />
    <small class="author">{epigram.author}</small>

</div>


<style>
.epigram {
    font-size: x-large;
}

.author {
    font-size: large;
}

</style>