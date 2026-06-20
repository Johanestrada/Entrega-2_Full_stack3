import { useEffect, useState } from 'react';

// Simple RSS -> cards feed component.
// Uses AllOrigins proxy to avoid CORS: https://api.allorigins.win/raw?url=<ENCODED_RSS>
// Props: feeds = [{ name, url }], maxItems

export default function NewsFeed({ feeds = [{ name: 'CNN', url: 'https://rss.cnn.com/rss/edition.rss' }], maxItems = 6 }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let mounted = true;

    async function fetchFeed(url, sourceName) {
      try {
        const proxy = `https://api.allorigins.win/raw?url=${encodeURIComponent(url)}`;
        const res = await fetch(proxy);
        if (!res.ok) throw new Error(`Error fetching ${sourceName}`);
        const text = await res.text();
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, 'text/xml');
        const channel = xml.querySelector('channel');
        if (!channel) return [];
        const list = Array.from(channel.querySelectorAll('item')).map((it) => {
          const titleEl = it.querySelector('title');
          const linkEl = it.querySelector('link');
          const descEl = it.querySelector('description');
          const pubEl = it.querySelector('pubDate');
          const title = titleEl ? titleEl.textContent : 'Sin título';
          const link = linkEl ? linkEl.textContent : (descEl ? (descEl.textContent.match(/href=\"([^\"]+)\"/) || [])[1] : '#');
          const description = descEl ? descEl.textContent : '';

          // try to extract first image from description/html
          const imgMatch = description.match(/<img[^>]+src=\"([^\"]+)\"/i);
          const image = imgMatch ? imgMatch[1] : null;

          return {
            source: sourceName,
            title,
            link,
            description: description.replace(/<[^>]+>/g, '').slice(0, 220),
            pubDate: pubEl ? pubEl.textContent : null,
            image,
          };
        });
        return list;
      } catch (err) {
        console.error(err);
        return [];
      }
    }

    (async () => {
      setLoading(true);
      setError(null);
      try {
        const all = [];
        for (const f of feeds) {
          const feedItems = await fetchFeed(f.url, f.name);
          all.push(...feedItems);
        }
        // sort by pubDate if present
        const sorted = all
          .filter(Boolean)
          .sort((a, b) => {
            const da = a.pubDate ? new Date(a.pubDate) : new Date(0);
            const db = b.pubDate ? new Date(b.pubDate) : new Date(0);
            return db - da;
          })
          .slice(0, maxItems);

        if (mounted) setItems(sorted);
      } catch (err) {
        if (mounted) setError('No se pudo cargar noticias');
      } finally {
        if (mounted) setLoading(false);
      }
    })();

    return () => {
      mounted = false;
    };
  }, [feeds, maxItems]);

  if (loading) return <div>Cargando noticias...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="row g-3">
      {items.map((it, i) => (
        <div className="col-md-4" key={`${it.title}-${i}`}>
          <div className="card h-100">
            {it.image && <img src={it.image} className="card-img-top" alt={it.title} style={{ height: 160, objectFit: 'cover' }} />}
            <div className="card-body d-flex flex-column">
              <h5 className="card-title">{it.title}</h5>
              <p className="card-text text-body-secondary flex-grow-1">{it.description}</p>
              <div className="mt-2 d-flex justify-content-between align-items-center">
                <small className="text-muted">{it.source}</small>
                <a href={it.link || '#'} target="_blank" rel="noopener noreferrer" className="btn btn-sm btn-primary">Ver noticia</a>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
