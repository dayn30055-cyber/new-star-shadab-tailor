(()=>{
  const slider=document.getElementById('serviceSlider');
  if(!slider)return;
  const cards=[...slider.querySelectorAll('article')];
  const heading=document.querySelector('#services .heading');
  if(heading){
    const eyebrow=heading.querySelector('.eyebrow');
    const title=heading.querySelector('h2');
    const desc=heading.querySelector('p:not(.eyebrow)');
    if(eyebrow)eyebrow.textContent='OUR SERVICES';
    if(title)title.innerHTML='Perfect Fit. <span>Perfect Style.</span>';
    if(desc)desc.textContent='Explore our tailoring services, crafted for every occasion.';
  }

  let dots=document.getElementById('serviceDots');
  if(!dots){
    dots=document.createElement('div');
    dots.id='serviceDots';
    dots.className='service-dots';
    dots.setAttribute('aria-label','Service slider pagination');
    slider.closest('.services-slider-shell')?.insertAdjacentElement('afterend',dots);
  }
  dots.innerHTML='';
  cards.forEach((_,i)=>{
    const b=document.createElement('button');
    b.type='button';
    b.className='service-dot'+(i===0?' active':'');
    b.setAttribute('aria-label',`Go to service ${i+1}`);
    b.addEventListener('click',()=>cards[i].scrollIntoView({behavior:'smooth',block:'nearest',inline:'start'}));
    dots.appendChild(b);
  });
  const dotEls=[...dots.children];
  const setActive=i=>dotEls.forEach((d,n)=>d.classList.toggle('active',n===i));
  const update=()=>{
    const left=slider.getBoundingClientRect().left;
    let best=0,dist=Infinity;
    cards.forEach((c,i)=>{const d=Math.abs(c.getBoundingClientRect().left-left);if(d<dist){dist=d;best=i;}});
    setActive(best);
  };
  slider.addEventListener('scroll',()=>requestAnimationFrame(update),{passive:true});
  window.addEventListener('resize',update,{passive:true});
  update();
})();