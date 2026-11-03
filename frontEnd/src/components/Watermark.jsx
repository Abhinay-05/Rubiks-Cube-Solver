import "./Watermark.css";

export default function Watermark() {
  return (
    <a
      className="watermark"
      href="https://www.linkedin.com/in/abhinay-anand-7721a5308/"
      target="_blank"
      rel="noopener noreferrer"
      aria-label="Visit Abhinay Anand's LinkedIn profile"
    >
      <span className="watermark-icon">in</span>
      <span>Built by Abhinay Anand</span>
    </a>
  );
}