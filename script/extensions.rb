class String
  def camelize
    self.gsub(/(.?(?<![a-zA-Z0-9])\w)/) { |c| c[-1].upcase }
  end

  def l_camelize
    camelize.sub(/^\w/) { |c| c.downcase }
  end

  def underscore
    self.camelize.gsub(/[[:upper:]]/) { |c| '_' + c.downcase }
      .sub(/^_/, '')
  end

  def dasherize
    underscore.gsub '_', '-'
  end
end

class FromHash
  def initialize(hash = {})
    hash.each do |k, y|
      if k.match?(/[0-9a-zA-Z_]+/) && respond_to?(k.to_s + '=')
        send k.to_s + '=', y
      end
    end
  end
end
