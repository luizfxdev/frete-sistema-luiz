'use client';

interface Props {
  title: string;
  value: number | string;
  icon: string | React.ReactNode;
  backgroundColor?: string;
  iconBackgroundColor?: string;
  textColor?: string;
  trend?: string;
  trendUp?: boolean;
}

export function KpiCard({
  title,
  value,
  icon,
  backgroundColor = 'bg-white',
  iconBackgroundColor = 'bg-gray-100',
  textColor = 'text-gray-600',
  trend,
  trendUp,
}: Props) {
  const isStringIcon = typeof icon === 'string';

  return (
    <div className={`${backgroundColor} rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow`}>
      <div className="flex items-start justify-between mb-4">
        <div className={`w-12 h-12 rounded-xl flex items-center justify-center ${iconBackgroundColor}`}>
          {isStringIcon ? (
            <i className={`bi ${icon} text-xl ${textColor}`}></i>
          ) : (
            <div className={`${textColor}`}>{icon}</div>
          )}
        </div>
        {trend && (
          <span className={`text-sm font-semibold ${trendUp ? 'text-green-600' : 'text-red-600'}`}>
            {trendUp ? '↑' : '↓'} {trend}
          </span>
        )}
      </div>
      <p className="text-gray-600 text-sm mb-2">{title}</p>
      <p className="text-3xl font-bold text-gray-900">{value}</p>
    </div>
  );
}