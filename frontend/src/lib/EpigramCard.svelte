<script lang="ts">
  export let epigram;
</script>

<div class="card">
  <div class="epigram">“{epigram.content}”</div>
  <div class="author">— {epigram.author}</div>
</div>

<style>
.card {
  text-align: center;
  padding: 2rem;
}

.epigram {
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 600;
  line-height: 1.2;
  margin-bottom: 1rem;
}

.author {
  font-size: clamp(1.4rem, 2.5vw, 2rem);
  font-style: italic;
  opacity: 0.8;
}
</style>