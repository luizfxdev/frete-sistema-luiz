interface Props {
  height?: number;
  className?: string;
}

export function LogoPreta({ height = 48, className }: Props) {
  const width = Math.round(height * (680 / 160));
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 680 160"
      xmlns="http://www.w3.org/2000/svg"
      className={className}
    >
      <g
        transform="translate(20,40)"
        stroke="#000000"
        strokeWidth="7"
        fill="none"
        strokeLinecap="round"
        strokeLinejoin="round"
      >
        <line x1="10" y1="80" x2="10" y2="10" />
        <line x1="10" y1="10" x2="80" y2="80" />
        <line x1="80" y1="80" x2="80" y2="10" />
        <circle cx="10" cy="10" r="5" fill="#000000" />
        <circle cx="80" cy="80" r="5" fill="#000000" />
        <circle cx="80" cy="10" r="5" fill="#000000" />
      </g>
      <text x="120" y="110" fontSize="80" fill="#000000">
        <tspan fontWeight="700">ext</tspan>
        <tspan fontWeight="400">Log</tspan>
      </text>
    </svg>
  );
}